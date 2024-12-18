import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import { useDispatch } from 'react-redux';

export default function DirectionTile(props) {
  const { children, style, gameId, point, controlled, direction } = props;
  const dispatch = useDispatch();
  const className = classnames(
    styles.tile,
    controlled ? styles.direction : styles.directionNonOwner
  );

  const onClick = React.useCallback(() => {
    if (gameId && direction) {
      dispatch(Actions.gameDir(gameId, direction));
    }
  }, [gameId, direction]);

  return (
    <div style={style} className={className} onClick={onClick}>
      {children}
    </div>
  );
};
