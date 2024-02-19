package ie.nok.api.graphql.adverts

import caliban.schema.Schema

enum PropertyType {
  case Apartment
  case Bungalow
  case Detached
  case Duplex
  case EndOfTerrace
  case House
  case SemiDetached
  case Site
  case Studio
  case Terraced
  case Unknown
}

object PropertyType {

  given Schema[Any, PropertyType] = Schema.Auto.derived

  def fromInternal(internal: ie.nok.adverts.PropertyType): PropertyType = internal match {
    case ie.nok.adverts.PropertyType.Apartment    => Apartment
    case ie.nok.adverts.PropertyType.Bungalow     => Bungalow
    case ie.nok.adverts.PropertyType.Detached     => Detached
    case ie.nok.adverts.PropertyType.Duplex       => Duplex
    case ie.nok.adverts.PropertyType.EndOfTerrace => EndOfTerrace
    case ie.nok.adverts.PropertyType.House        => House
    case ie.nok.adverts.PropertyType.SemiDetached => SemiDetached
    case ie.nok.adverts.PropertyType.Site         => Site
    case ie.nok.adverts.PropertyType.Studio       => Studio
    case ie.nok.adverts.PropertyType.Terraced     => Terraced
  }
}
