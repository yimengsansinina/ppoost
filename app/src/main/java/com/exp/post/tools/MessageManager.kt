package com.exp.post.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.exp.post.bean.MessageInfo
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object MessageManager {
    
    fun handleMessageInfo(context: Context, messageInfo: MessageInfo?) {
        if (messageInfo == null || (messageInfo.messageId == 0 && messageInfo.messageText.isNullOrEmpty())) {
            return
        }

        // 检查是否需要显示消息
        if (shouldShowMessage(messageInfo)) {
            showMessageDialog(context, messageInfo)
        }
    }

    private fun shouldShowMessage(messageInfo: MessageInfo): Boolean {
        // messageId 为 0，不显示
        if (messageInfo.messageId == 0) {
            return false
        }

        // messageId 为 -1，总是显示
        if (messageInfo.messageId == -1) {
            return true
        }

        // 比较本地保存的 messageId，如果不同则显示
        val lastMessageId = MMKVManager.getLastMessageId()
        return messageInfo.messageId != lastMessageId
    }

    private fun showMessageDialog(context: Context, messageInfo: MessageInfo) {
        MaterialAlertDialogBuilder(context)
            .setMessage(messageInfo.messageText)
            .setPositiveButton("确定") { dialog, _ ->
                dialog.dismiss()
                // 如果不是 -1，保存新的 messageId
                if (messageInfo.messageId != -1) {
                    MMKVManager.saveLastMessageId(messageInfo.messageId)
                }
                // 处理URL跳转
                messageInfo.messageUrl?.takeIf { it.isNotEmpty() }?.let { url ->
                    openBrowser(context, url)
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun openBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(context, "无法打开链接", Toast.LENGTH_SHORT).show()
        }
    }
} 