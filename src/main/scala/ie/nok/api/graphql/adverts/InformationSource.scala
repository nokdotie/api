package ie.nok.api.graphql.adverts

import caliban.schema.Schema

enum InformationSource {
  case DaftIeAdvert(url: String)
  case DngIeAdvert(url: String)
  case MyHomeIeAdvert(url: String)
  case PropertyPalComAdvert(url: String)
  case SherryFitzIeAdvert(url: String)
  case BuildingEnergyRatingCertificate(
      url: String,
      number: Int,
      rating: String,
      energyRatingInKWhPerSqtMtrPerYear: Float,
      carbonDioxideEmissionsIndicatorInKgCO2PerSqtMtrPerYear: Float
  )
}

object InformationSource {
  given Schema[Any, InformationSource] = Schema.Auto.derived

  def fromInternal(
      internal: ie.nok.adverts.InformationSource
  ): InformationSource =
    internal match {
      case ie.nok.adverts.InformationSource.DaftIeAdvert(value) =>
        InformationSource.DaftIeAdvert(value.url)
      case ie.nok.adverts.InformationSource.DngIeAdvert(value) =>
        InformationSource.DngIeAdvert(value.url)
      case ie.nok.adverts.InformationSource.MyHomeIeAdvert(value) =>
        InformationSource.MyHomeIeAdvert(value.url)
      case ie.nok.adverts.InformationSource.PropertyPalComAdvert(value) =>
        InformationSource.PropertyPalComAdvert(value.url)
      case ie.nok.adverts.InformationSource.SherryFitzIeAdvert(value) =>
        InformationSource.SherryFitzIeAdvert(value.url)
      case ie.nok.adverts.InformationSource
            .BuildingEnergyRatingCertificate(value) =>
        InformationSource.BuildingEnergyRatingCertificate(
          value.url,
          value.number.value,
          value.rating.toString,
          value.energyRating.value,
          value.carbonDioxideEmissionsIndicator.value
        )
    }
}
