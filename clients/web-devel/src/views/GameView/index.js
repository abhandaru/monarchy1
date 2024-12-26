import * as Actions from '~/state/actions';
import * as React from 'react';
import Board from './Board';
import NavigationView from '~/components/layout/NavigationView';
import PhasesView from './PhasesView';
import Summary from './Summary';
import styles from './index.css';
import { useSelector, useDispatch } from 'react-redux';

const GameView = (props) => {
  const { gameId } = props.match.params;
  const dispatch = useDispatch();
  const userId = useSelector(_ => _.auth.userId);
  const game = useSelector(_ => _.games.game);
  const player = game?.players.find(_ => _.user.id === userId);

  // componentDidMount
  React.useEffect(() => {
    dispatch(Actions.gameFetch(gameId)).then(r => {
      const point = r.state.currentSelection.selection;
      return point && dispatch(Actions.gameSelect(gameId, point));
    })
  }, []);

  return (
    <>
      <NavigationView />
      <div className={styles.root}>
        {game ?
          <>
            <Summary selections={game.state.currentSelection} />
            <PhasesView gameId={gameId} className={styles.phase} />
            <Board
              gameId={game.id}
              playerId={player?.id}
              currentSelection={game.state.currentSelection.selection}
              currentPlayerId={game.state.currentPlayerId}
              tiles={game.state.tiles}
            />
          </> : null
        }
      </div>
    </>
  );
};

export default GameView;
