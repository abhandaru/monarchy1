package monarchy.marshalling

import monarchy.game._

object PieceConfStringBijection extends StringBijection[PieceConf] {
  override def apply(conf: PieceConf): String = conf.toString
  override def invert(json: String): PieceConf = json match {
    case "Assassin" => Assassin
    case "Knight" => Knight
    case "Scout" => Scout
    case "Witch" => Witch
    case "Pyromancer" => Pyromancer
    case "MudGolem" => MudGolem
    case "Furgon" => Furgon
    case "Cleric" => Cleric
    case "FrostGolem" => FrostGolem
    case "Shrub" => Shrub
  }
}
