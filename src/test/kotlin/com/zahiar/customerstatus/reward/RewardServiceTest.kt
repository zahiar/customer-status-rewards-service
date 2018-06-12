package com.zahiar.customerstatus.reward

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.zahiar.customerstatus.eligibility.EligibilityConstants
import com.zahiar.customerstatus.eligibility.EligibilityInterface
import com.zahiar.customerstatus.eligibility.InvalidAccountNumberException
import com.zahiar.customerstatus.eligibility.TechnicalFailureException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RewardServiceTest {

    lateinit var eligibilityService: EligibilityInterface
    lateinit var rewardService: RewardService

    @BeforeEach
    fun setUp() {
        eligibilityService = mock()
        rewardService = RewardService(eligibilityService)
    }

    @Nested inner class `Given a customer is eligible for rewards` {
        @Nested inner class `When they have zero channel subscriptions` {

            @Test
            fun `Then no rewards should be returned`() {
                val expected =  mapOf("message" to "", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(123, emptyList())

                assertEquals(expected, actual)
            }

        }

        @Nested inner class `When they have channel subscriptions with no eligible rewards` {

            @Test
            fun `Then no rewards should be returned`() {
                whenever(eligibilityService.getCustomerEligibility(123))
                    .thenReturn(EligibilityConstants.CUSTOMER_ELIGIBLE)

                val expected =  mapOf("message" to "", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(123, listOf("NEWS", "KIDS"))

                assertEquals(expected, actual)
            }

        }

        @Nested inner class `When they have channel subscriptions with eligible rewards` {

            @Test
            fun `Then a list of all the rewards should be returned`() {
                whenever(eligibilityService.getCustomerEligibility(123))
                    .thenReturn(EligibilityConstants.CUSTOMER_ELIGIBLE)

                val expected = mapOf(
                    "message" to "",
                    "rewards" to listOf(
                        "CHAMPIONS_LEAGUE_FINAL_TICKET",
                        "KARAOKE_PRO_MICROPHONE",
                        "PIRATES_OF_THE_CARIBBEAN_COLLECTION"
                    )
                )
                val actual = rewardService.getRewardsForCustomer(
                    123,
                    listOf("SPORTS", "KIDS", "MUSIC", "NEWS", "MOVIES")
                )

                assertEquals(expected, actual)
            }

        }

        @Nested inner class `When they have duplicate channel subscriptions with eligible rewards` {

            @Test
            fun `Then a list of all the unique rewards should be returned`() {
                whenever(eligibilityService.getCustomerEligibility(123))
                    .thenReturn(EligibilityConstants.CUSTOMER_ELIGIBLE)

                val expected = mapOf(
                    "message" to "",
                    "rewards" to listOf(
                        "CHAMPIONS_LEAGUE_FINAL_TICKET",
                        "KARAOKE_PRO_MICROPHONE"
                    )
                )
                val actual = rewardService.getRewardsForCustomer(
                    123,
                    listOf("SPORTS", "SPORTS", "MUSIC", "MUSIC")
                )

                assertEquals(expected, actual)
            }

        }
    }

    @Nested inner class `Given a customer is ineligible for rewards` {
        @Nested inner class `When they have zero channel subscriptions` {

            @Test
            fun `Then no rewards should be returned`() {
                val expected = mapOf("message" to "", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(123, emptyList())

                assertEquals(expected, actual)
            }

        }

        @Nested inner class `When they have channel subscriptions with eligible rewards` {

            @Test
            fun `Then no rewards should be returned`() {
                whenever(eligibilityService.getCustomerEligibility(123))
                    .thenReturn(EligibilityConstants.CUSTOMER_INELIGIBLE)

                val expected = mapOf("message" to "", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(123, listOf("MUSIC"))

                assertEquals(expected, actual)
            }

        }
    }

    @Nested inner class `Given any customer` {
        @Nested inner class `When the EligibilityService has a technical failure` {

            @Test
            fun `Then no rewards should be returned`() {
                whenever(eligibilityService.getCustomerEligibility(123))
                    .thenThrow(TechnicalFailureException())

                val expected = mapOf("message" to "", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(123, listOf("MUSIC"))

                assertEquals(expected, actual)
            }

        }
    }

    @Nested inner class `Given an invalid customer's account number` {
        @Nested inner class `When the EligibilityService throws an InvalidAccountNumber exception` {

            @Test
            fun `Then only 'Invalid account number' is returned`() {
                whenever(eligibilityService.getCustomerEligibility(123))
                    .thenThrow(InvalidAccountNumberException())

                val expected = mapOf("message" to "Invalid account number", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(123, listOf("MUSIC"))

                assertEquals(expected, actual)
            }

        }
    }

}