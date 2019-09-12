package monarchy.dalwrite

import monarchy.dal

case class GameNode(
  data: dal.Game,
  players: Seq[dal.Player]
)
