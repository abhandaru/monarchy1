import { State } from '~/state/state';
import { useSelector } from 'react-redux';

// TODO: This is a temporary type until we have a proper type generation from GQL.
type Player = {
  id: string;
  user: {
    id: string;
    username: string;
  };
};

export default function useCurrentPlayer(): Player | null {
  // Extract players from the game state and then filter for a player with a
  // matching user ID.
  const players = useSelector<State, Player[]>(_ => _.games.game.players);
  const currentPlayerId = useSelector<State, String | null>(_ => _.games.game.state.currentPlayerId);
  return players.find(_ => _.id === currentPlayerId);
}