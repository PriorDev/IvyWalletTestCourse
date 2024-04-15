package com.ivy.core.domain.action.transaction

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.ivy.common.time.provider.TimeProvider
import com.ivy.core.domain.action.Action
import com.ivy.core.domain.action.WriteTrnsAct.TimeProviderFake
import com.ivy.core.domain.action.WriteTrnsAct.TransactionDaoFake
import com.ivy.core.domain.algorithm.accountcache.InvalidateAccCacheAct
import com.ivy.core.persistence.algorithm.accountcache.AccountCacheDao
import com.ivy.core.persistence.dao.trn.TransactionDao
import com.ivy.data.Sync
import com.ivy.data.SyncState
import com.ivy.data.Value
import com.ivy.data.account.Account
import com.ivy.data.account.AccountState
import com.ivy.data.attachment.Attachment
import com.ivy.data.attachment.AttachmentSource
import com.ivy.data.attachment.AttachmentType
import com.ivy.data.tag.Tag
import com.ivy.data.tag.TagState
import com.ivy.data.transaction.Transaction
import com.ivy.data.transaction.TransactionType
import com.ivy.data.transaction.TrnMetadata
import com.ivy.data.transaction.TrnPurpose
import com.ivy.data.transaction.TrnState
import com.ivy.data.transaction.TrnTime
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class WriteTrnsActTest {
    private lateinit var writeTrnsAct: WriteTrnsAct
    private lateinit var transactionDao: TransactionDaoFake
    private lateinit var timeProvider: TimeProviderFake
    private lateinit var accountCacheDao: AccountCacheDaoFake

    @BeforeEach
    fun setUp() {
        transactionDao = TransactionDaoFake()
        timeProvider = TimeProviderFake()
        accountCacheDao = AccountCacheDaoFake()

        writeTrnsAct = WriteTrnsAct(
            transactionDao = transactionDao,
            trnsSignal = TrnsSignal(),
            timeProvider = timeProvider,
            invalidateAccCacheAct = InvalidateAccCacheAct(
                accountCacheDao = accountCacheDao,
                timeProvider = timeProvider
            ),
            accountCacheDao = accountCacheDao
        )
    }

    @Test
    fun `Test create transaction with expense`() = runBlocking<Unit> {
        val account = account().copy(
            name = "Specific name"
        )

        val transactionId = UUID.randomUUID()
        val tag = tag()
        val attachment = attachment(transactionId)
        val transaction = transaction(account).copy(
            id = transactionId,
            tags = listOf(tag),
            attachments = listOf(attachment)
        )

        writeTrnsAct(WriteTrnsAct.Input.CreateNew(transaction))

        val cachedTransaction = transactionDao.transactions.find {
            it.id == transactionId.toString()
        }
        val cachedTag = transactionDao.tags.find { it.tagId == tag.id }
        val cachedAttachement = transactionDao.attachment.find { it.id == attachment.id }

        assertThat(cachedTransaction).isNotNull()
        assertThat(cachedTransaction?.type).isEqualTo(TransactionType.Expense)

        assertThat(cachedTag).isNotNull()
        assertThat(cachedAttachement).isNotNull()
    }

}