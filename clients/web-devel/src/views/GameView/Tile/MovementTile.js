import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import { useDispatch } from 'react-redux';

export default function MovementTile(props) {
  const { children, style, gameId, point, controlled } = props;
  const className = classnames(
    styles.tile,
    controlled ? styles.movement : styles.movementNonOwner
  );

  const dispatch = useDispatch();
  const onClick = React.useCallback(() => {
    if (gameId) {
      dispatch(Actions.gameMove(gameId, point));
    }
  }, [gameId, point]);

  return (
    <div style={style} className={className} onClick={onClick}>
      {children}
    </div>
  );
};
