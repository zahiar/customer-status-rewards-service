package com.zahiar.customerstatus.eligibility

class DummyEligibilityService : EligibilityInterface {

    override fun getCustomerEligibility(accountNumber: Int) = when (accountNumber) {
        123 -> EligibilityConstants.CUSTOMER_ELIGIBLE
        456 -> EligibilityConstants.CUSTOMER_INELIGIBLE
        0 -> throw InvalidAccountNumberException()
        else -> throw TechnicalFailureException()
    }

}