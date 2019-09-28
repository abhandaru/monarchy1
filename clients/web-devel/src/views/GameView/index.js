import * as React from 'react';
import Board from './Board';
import classnames from 'classnames';
import NavigationView from '~/components/layout/NavigationView';
import PhaseBar from './PhaseBar';
import styles from './index.css';
import Tile from './Tile';
import { gameFetch } from '~/state/actions';
import { useSelector, useDispatch } from 'react-redux';

const GameView = (props) => {
  const { gameId } = props.match.params;
  const dispatch = useDispatch();
  const game = useSelector(_ => _.games.game);

  // componentDidMount
  React.useEffect(() => {
    dispatch(gameFetch(gameId));
  }, []);

  return (
    <>
      <NavigationView />
      <div className={styles.root}>
        {game ?
          <>
            <PhaseBar className={styles.phase} />
            <Board
              gameId={game.id}
              currentSelection={game.state.currentSelection}
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
