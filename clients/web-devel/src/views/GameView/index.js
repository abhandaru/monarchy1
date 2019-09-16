import * as React from 'react';
import NavigationView from '~/components/layout/NavigationView';
import styles from './index.css';
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
        <h3>Your game here</h3>
        <p>{JSON.stringify(game)}</p>
      </div>
    </>
  );
};

export default GameView;
