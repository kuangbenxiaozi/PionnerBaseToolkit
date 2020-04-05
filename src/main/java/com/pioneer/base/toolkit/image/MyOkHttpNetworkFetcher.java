package com.pioneer.base.toolkit.image;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.os.SystemClock;

import com.facebook.common.logging.FLog;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MyOkHttpNetworkFetcher extends OkHttpNetworkFetcher {

    private static final String TAG = "WMOkHttpNetworkFetchProducer";

    private final OkHttpClient mOkHttpClient;
    private Executor mCancellationExecutor;
    private Context mContext;

    public MyOkHttpNetworkFetcher(OkHttpClient okHttpClient,Context context) {
        super(okHttpClient);
        mContext = context;
        mOkHttpClient = okHttpClient;
        mCancellationExecutor = okHttpClient.dispatcher().executorService();
    }


    @Override
    public void fetch(final OkHttpNetworkFetchState fetchState, final Callback callback) {
        fetchState.submitTime = SystemClock.elapsedRealtime();
        final Uri uri = fetchState.getUri();
        final Request.Builder builder = new Request.Builder()
                .cacheControl(new CacheControl.Builder().noStore().build())
                .url(uri.toString())
                .get();

        final Request request = addHeader(builder).build();

        final Call call = mOkHttpClient.newCall(request);

        fetchState.getContext().addCallbacks(
                new BaseProducerContextCallbacks() {
                    @Override
                    public void onCancellationRequested() {
                        if (Looper.myLooper() != Looper.getMainLooper()) {
                            call.cancel();
                        } else {
                            mCancellationExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    call.cancel();
                                }
                            });
                        }
                    }
                });

        call.enqueue(
                new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        fetchState.responseTime = SystemClock.elapsedRealtime();
                        if (!response.isSuccessful()) {
                            handleException(call, new IOException("Unexpected HTTP code " + response), callback);
                            return;
                        }
                        final ResponseBody body = response.body();
                        try {
                            long contentLength = body.contentLength();
                            if (contentLength < 0) {
                                contentLength = 0;
                            }
                            callback.onResponse(body.byteStream(), (int) contentLength);
                        } catch (Exception e) {
                            handleException(call, e, callback);
                        } finally {
                            try {
                                body.close();
                            } catch (Exception e) {
                                FLog.w(TAG, "Exception when closing response body", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call,IOException e) {
                        handleException(call, e, callback);
                    }
                });
    }

    private Request.Builder addHeader(Request.Builder builder) {
        return builder;
    }

    /**
     * Handles exceptions.
     * <p>
     * <p> OkHttp notifies callers of cancellations via an IOException. If IOException is caught
     * after request cancellation, then the exception is interpreted as successful cancellation
     * and onCancellation is called. Otherwise onFailure is called.
     */
    private void handleException(final Call call, final Exception e, final Callback callback) {
        if (call.isCanceled()) {
            callback.onCancellation();
        } else {
            callback.onFailure(e);
        }
    }
}

