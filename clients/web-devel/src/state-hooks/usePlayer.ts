import { State, Player } from '~/state/state';
import { useSelector } from 'react-redux';

// Extract players from the game state and then filter for a player with a
// matching ID.
export default function usePlayer(playerId: string): Player | null {
  const players = useSelector<State, Player[]>(_ => _.games.game.players);
  return players.find(_ => _.id === playerId);
}