import { State } from '~/state/state';
import { useSelector } from 'react-redux';
import useCurrentPlayer from '~/state-hooks/useCurrentPlayer';

// Grab the viewer ID from the auth state. Then compare to the fully hydrated 
// player data in the game state.
export default function useMyTurn(): Boolean {
  const userId = useSelector<State, String | null>(_ => _.auth.userId);
  const currentPlayer = useCurrentPlayer();
  return currentPlayer?.user.id === userId;
}
