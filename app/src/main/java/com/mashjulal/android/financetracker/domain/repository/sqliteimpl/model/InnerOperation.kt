package com.mashjulal.android.financetracker.domain.repository.sqliteimpl.model

import com.google.auto.value.AutoValue
import com.mashjulal.android.financetracker.domain.repository.sqlmodel.OperationModel
import com.squareup.sqldelight.RowMapper

@AutoValue
abstract class InnerOperation : OperationModel {
    companion object {

        @AutoValue
        abstract class JointByIdInfo : OperationModel.SelectOperationByIdModel<InnerOperation, InnerCategory, InnerAccount>

        @AutoValue
        abstract class JointAllInfo : OperationModel.SelectAllModel<InnerOperation, InnerCategory, InnerAccount>

        @AutoValue
        abstract class JointByDateRange : OperationModel.SelectByDateRangeModel<InnerOperation, InnerCategory, InnerAccount>

        @AutoValue
        abstract class JointByAccount : OperationModel.SelectByAccountModel<InnerOperation, InnerCategory, InnerAccount>

        @AutoValue
        abstract class JointByCategory : OperationModel.SelectByCategoryModel<InnerOperation, InnerCategory, InnerAccount>

        val FACTORY: OperationModel.Factory<InnerOperation> =
                OperationModel.Factory(OperationModel.Creator<InnerOperation> { id, dt, sum, currency, category, account, ratio ->
                    AutoValue_InnerOperation(id, dt, sum, currency, category, account, ratio)
                })

        val SELECT_OPERATION_BY_ID: RowMapper<JointByIdInfo> =
                FACTORY.selectOperationByIdMapper(::AutoValue_InnerOperation_Companion_JointByIdInfo, InnerCategory.FACTORY, InnerAccount.FACTORY)

        val ALL_OPERATIONS_MAPPER: RowMapper<JointAllInfo> =
                FACTORY.selectAllMapper(::AutoValue_InnerOperation_Companion_JointAllInfo, InnerCategory.FACTORY, InnerAccount.FACTORY)

        val SELECT_OPS_BY_DATE_RANGE: RowMapper<JointByDateRange> =
                FACTORY.selectByDateRangeMapper(::AutoValue_InnerOperation_Companion_JointByDateRange, InnerCategory.FACTORY, InnerAccount.FACTORY)

        val SELECT_OPS_BY_ACCOUNT: RowMapper<JointByAccount> =
                FACTORY.selectByAccountMapper(::AutoValue_InnerOperation_Companion_JointByAccount, InnerCategory.FACTORY, InnerAccount.FACTORY)

        val SELECT_OPS_BY_CATEGORY: RowMapper<JointByCategory> =
                FACTORY.selectByCategoryMapper(::AutoValue_InnerOperation_Companion_JointByCategory, InnerCategory.FACTORY, InnerAccount.FACTORY)

    }

}
