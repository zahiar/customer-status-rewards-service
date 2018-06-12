package com.zahiar.customerstatus.reward

import com.zahiar.customerstatus.eligibility.EligibilityConstants
import com.zahiar.customerstatus.eligibility.EligibilityInterface
import com.zahiar.customerstatus.eligibility.InvalidAccountNumberException
import com.zahiar.customerstatus.eligibility.TechnicalFailureException

class RewardService(private val eligibilityService: EligibilityInterface) {

    private val rewards = mapOf(
        "SPORTS" to "CHAMPIONS_LEAGUE_FINAL_TICKET",
        "MUSIC" to "KARAOKE_PRO_MICROPHONE",
        "MOVIES" to "PIRATES_OF_THE_CARIBBEAN_COLLECTION"
    )

    fun getRewardsForCustomer(accountNumber: Int, channelSubscriptions: List<String>): Map<String, Any> {
        val returnValues = mutableMapOf(
            "message" to "",
            "rewards" to emptyList<String>()
        )

        if (channelSubscriptions.isEmpty()) {
            return returnValues
        }

        try {
            if (eligibilityService.getCustomerEligibility(accountNumber) === EligibilityConstants.CUSTOMER_ELIGIBLE) {
                returnValues.replace(
                    "rewards",
                    rewards.filterKeys { channelSubscriptions.contains(it) }.values.toList()
                )

                return returnValues
            }
        } catch (exception: TechnicalFailureException) {
            return returnValues
        } catch (exception: InvalidAccountNumberException) {
            returnValues.replace("message", "Invalid account number")
            return returnValues
        }

        return returnValues
    }

}