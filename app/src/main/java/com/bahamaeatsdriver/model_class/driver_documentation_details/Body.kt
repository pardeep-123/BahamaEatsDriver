package com.bahamaeatsdriver.model_class.driver_documentation_details

data class Body(
    val carInsurance: String,
    val cardBackPhoto: String,
    val cardFrontPhoto: String,
    val cardId: Int,
    val isCarInsuranceApproved: Int,
    val isDriverIdApproved: Int,
    val isDriverLicenseApproved: Int,
    val isPoliceReportApproved: Int,
    val licenseBackPhoto: String,
    val licenseFrontPhoto: String,
    val licenseId: Int,
    val policeRecord: String
)