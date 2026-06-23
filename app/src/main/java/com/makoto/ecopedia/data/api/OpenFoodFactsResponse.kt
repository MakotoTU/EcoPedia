package com.makoto.ecopedia.data.api

import com.google.gson.annotations.SerializedName

data class OpenFoodFactsResponse(
    @SerializedName("code") val code: String?,
    @SerializedName("product") val product: ProductData?,
    @SerializedName("status") val status: Int?,
    @SerializedName("status_verbose") val statusVerbose: String?
)

data class ProductData(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("packaging") val packaging: PackagingData?,
    @SerializedName("packaging_materials_tags") val packagingMaterialsTags: List<String>?,
    @SerializedName("ecoscore_grade") val ecoscoreGrade: String?
)

data class PackagingData(
    @SerializedName("text") val text: String?
)
