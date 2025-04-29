package org.chorus_oss.chorus.permission


interface PermissionRemovedExecutor {
    fun attachmentRemoved(attachment: PermissionAttachment?)
}
