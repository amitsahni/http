package webconnect.com.webconnect.listener

import android.support.annotation.Nullable
import webconnect.com.webconnect.model.SuccessModel

interface OnSuccessListener<T> {
    fun onSuccess(model: T)
}