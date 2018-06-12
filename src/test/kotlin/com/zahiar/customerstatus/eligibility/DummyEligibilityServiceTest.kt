package com.zahiar.customerstatus.eligibility

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DummyEligibilityServiceTest {

    lateinit var service: DummyEligibilityService

    @BeforeEach
    fun setup() {
        service = DummyEligibilityService()
    }

    @Nested inner class `Given a valid customer's account number` {
        @Nested inner class `When they are deemed to be eligible for rewards` {

            @Test
            fun `Then the service should return 'CUSTOMER_ELIGIBLE'`() {
                assertEquals("CUSTOMER_ELIGIBLE", service.getCustomerEligibility(123))
            }

        }

        @Nested inner class `When they are deemed to be ineligible for rewards` {

            @Test
            fun `Then the service should return 'CUSTOMER_INELIGIBLE'`() {
                assertEquals("CUSTOMER_INELIGIBLE", service.getCustomerEligibility(456))
            }

        }
    }

    @Nested inner class `Given an invalid customer's account number` {
        @Nested inner class `When the eligbility service is called` {

            @Test
            fun `Then an 'InvalidAccountNumber' exception should be thrown`() {
                assertThrows<InvalidAccountNumberException> { service.getCustomerEligibility(0) }
            }

        }
    }

    @Nested inner class `Given a customer's account number` {
        @Nested inner class `When the eligibility service is experiencing technical issues` {

            @Test
            fun `Then a 'TechnicalFailure' exception should be thrown`() {
                assertThrows<TechnicalFailureException> { service.getCustomerEligibility(999) }
            }

        }
    }

}