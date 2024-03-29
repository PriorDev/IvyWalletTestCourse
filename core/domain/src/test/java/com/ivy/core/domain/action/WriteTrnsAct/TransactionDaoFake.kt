package com.ivy.core.domain.action.WriteTrnsAct

import androidx.sqlite.db.SupportSQLiteQuery
import arrow.core.ifThen
import com.ivy.core.persistence.dao.trn.AccountIdAndTrnTime
import com.ivy.core.persistence.dao.trn.SaveTrnData
import com.ivy.core.persistence.dao.trn.TransactionDao
import com.ivy.core.persistence.entity.attachment.AttachmentEntity
import com.ivy.core.persistence.entity.trn.TransactionEntity
import com.ivy.core.persistence.entity.trn.TrnMetadataEntity
import com.ivy.core.persistence.entity.trn.TrnTagEntity
import com.ivy.data.DELETING
import com.ivy.data.SyncState

class TransactionDaoFake: TransactionDao() {
    val transactions = mutableListOf<TransactionEntity>()
    val tags = mutableListOf<TrnTagEntity>()
    val attachment = mutableListOf<AttachmentEntity>()
    val metaData = mutableListOf<TrnMetadataEntity>()
    override suspend fun saveTrnEntity(entity: TransactionEntity) {
        transactions.add(entity)
    }

    override suspend fun updateTrnTagsSyncByTrnId(trnId: String, sync: SyncState) {
        val index = transactions.indexOfFirst { it.id == trnId }
        if (index == -1) return
        transactions[index] = transactions[index].copy(sync = sync)
    }

    override suspend fun saveTags(entity: List<TrnTagEntity>) {
        tags.addAll(entity)
    }

    override suspend fun updateAttachmentsSyncByAssociatedId(
        associatedId: String,
        sync: SyncState
    ) {
        val index = attachment.indexOfFirst { it.associatedId == associatedId }
        if (index == -1) return
        attachment[index] = attachment[index].copy(sync = sync)
    }

    override suspend fun saveAttachments(entity: List<AttachmentEntity>) {
        attachment.addAll(entity)
    }

    override suspend fun updateMetadataSyncByTrnId(trnId: String, sync: SyncState) {
        val index = metaData.indexOfFirst { it.trnId == trnId }
        if (index == -1) return
        metaData[index] = metaData[index].copy(sync = sync)
    }

    override suspend fun saveMetadata(entity: List<TrnMetadataEntity>) {
        metaData.addAll(entity)
    }

    override suspend fun findAllBlocking(): List<TransactionEntity> {
        return transactions.filter { it.sync.code != DELETING }
    }

    override suspend fun findBySQL(query: SupportSQLiteQuery): List<TransactionEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun findAccountIdAndTimeById(trnId: String): AccountIdAndTrnTime? {
        TODO("Not yet implemented")
    }

    override suspend fun updateTrnEntitySyncById(trnId: String, sync: SyncState) {
        TODO("Not yet implemented")
    }
}