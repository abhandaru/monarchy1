package monarchy.graphql

import monarchy.game.{Game, Vec}

case class Selection(
  game: Game,
  movements: Set[Vec] = Set.empty,
  directions: Set[Vec] = Set.empty,
  attacks: Set[Set[Vec]] = Set.empty
)

object Selection {
  private[graphql] def apply(game: Game): Selection = {
    Selection(
      game = game,
      movements = game.movements,
      directions = game.directions,
      attacks = game.attacks,
    )
  }
}
