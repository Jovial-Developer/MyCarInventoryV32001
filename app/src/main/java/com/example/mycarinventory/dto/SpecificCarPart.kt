package com.example.mycarinventory.dto

data class SpecificCarPart(var partId : Int = 0,
                           var thisPartName :String = "",
                           var thisPartIdFirebase : String = "",
                           var thisPartModel : String = "",
                           var thisPartBrand : String = "",
                           var thisPartsCarMake : String = "",
                           var thisPartPrice : String = ""
)
{
    override fun toString(): String {
        return "$thisPartName, $thisPartModel, $thisPartBrand, $thisPartsCarMake, $thisPartPrice"
    }
}