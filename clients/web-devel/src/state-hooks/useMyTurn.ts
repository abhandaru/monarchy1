import { State } from '~/state/state';
import { useSelector } from 'react-redux';

export default function useMyTurn(): Boolean {
  // Grab the viewer ID from the auth state.
  const userId = useSelector<State, String | null>(_ => _.auth.userId);

  // Extract players from the game state and then filter for a player with a
  // matching user ID.
  const players = useSelector<State, any[]>(_ => _.games.game.players);
  const playerId = players.find(_ => _.user.id === userId)?.id;
  const currentPlayerId = useSelector<State, String | null>(_ => _.games.game.state.currentPlayerId);

  // Finally, compare these two IDs.
  return playerId === currentPlayerId;
}