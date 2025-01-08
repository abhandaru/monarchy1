import * as Types from '~/util/types';
import { State } from '~/state/state';
import { useSelector } from 'react-redux';
import usePlayer from '~/state-hooks/usePlayer';

export default function useCurrentPlayer(): Types.Player | null {
  const currentPlayerId = useSelector<State, string | null>(_ => _.games.game.state.currentPlayerId);
  const currentPlayer = currentPlayerId ? usePlayer(currentPlayerId) : null;
  return currentPlayer;
}