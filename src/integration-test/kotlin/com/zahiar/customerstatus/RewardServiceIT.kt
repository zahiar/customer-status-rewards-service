package com.zahiar.customerstatus

import com.zahiar.customerstatus.eligibility.DummyEligibilityService
import com.zahiar.customerstatus.eligibility.EligibilityInterface
import com.zahiar.customerstatus.reward.RewardService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RewardServiceIT {

    lateinit var eligibilityService: EligibilityInterface
    lateinit var rewardService: RewardService

    @BeforeEach
    fun setUp() {
        eligibilityService = DummyEligibilityService()
        rewardService = RewardService(eligibilityService)
    }

    @Nested inner class `Given a customer is eligible for rewards` {
        @Nested inner class `When they have subscriptions with eligible rewards` {

            @Test
            fun `Then a list of all the rewards should be returned`() {
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
    }

    @Nested inner class `Given a customer is ineligible for rewards` {
        @Nested inner class `When they have subscriptions eligible for rewards` {

            @Test
            fun `Then no rewards should be returned`() {
                val expected =  mapOf("message" to "", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(456, listOf("SPORTS", "MUSIC"))

                assertEquals(expected, actual)
            }

        }
    }

    @Nested inner class `Given any customer` {
        @Nested inner class `When the EligibilityService has technical issues` {

            @Test
            fun `Then no rewards should be returned`() {
                val expected =  mapOf("message" to "", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(999, listOf("SPORTS", "MUSIC"))

                assertEquals(expected, actual)
            }

        }

    }

    @Nested inner class `Given an invalid customer's account number` {
        @Nested inner class `When the RewardsService is called` {

            @Test
            fun `Then only 'Invalid account number' is returned`() {
                val expected = mapOf("message" to "Invalid account number", "rewards" to emptyList<String>())
                val actual = rewardService.getRewardsForCustomer(0, listOf("MUSIC"))

                assertEquals(expected, actual)
            }

        }
    }

}