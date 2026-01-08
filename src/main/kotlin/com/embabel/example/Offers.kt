package com.embabel.example

import com.embabel.example.investmentmanager.model.Offer

object Offers {

    public val dummyOffers =
        listOf(
            Offer(
                "off1",
                "Personalized Strategy",
                "Get a free 1-on-1 advisor session",
                "Free Session"
            ),
            Offer(
                "off2",
                "Tax-Loss Harvesting",
                "Let AI optimize your tax liability",
                "Save up to $2k"
            ),
            Offer(
                "off3",
                "Venture Access",
                "Early access to Series B startup funding",
                "Exclusive"
            ),
            Offer(
                "off4",
                "High-Yield Savings",
                "Upgrade to Platinum for 5.5% APY",
                "Interest Bonus"
            )
        )

}