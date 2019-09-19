import * as React from 'react';
import classnames from 'classnames';
import NavigationView from '~/components/layout/NavigationView';
import PhaseBar from './PhaseBar';
import streamProxy from '~/api/streamProxy';
import styles from './index.css';
import Board from './Board';
import { gameFetch } from '~/state/actions';
import { useSelector, useDispatch } from 'react-redux';

const Game = (props) => {
  const { game } = props;
  const { id, currentSelection, currentPlayerId, state } = game;
  const { tiles, phase } = state;
  return (
    <>
      <PhaseBar />
      <Board
        gameId={id}
        currentSelection={state.currentSelection}
        currentPlayerId={state.currentPlayerId}
        tiles={state.tiles}
      />
    </>
  );
};

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
        {game ? <Game game={game} /> : null}
      </div>
    </>
  );
};

export default GameView;
