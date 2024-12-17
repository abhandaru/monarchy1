import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import { useDispatch, useSelector } from 'react-redux';

export default function AttackTile(props) {
  const { children, style, gameId, point, controlled } = props;
  const dispatch = useDispatch();
  const className = classnames(
    styles.tile,
    controlled ? styles.attack : styles.attackNonOwner
  );

  const onClick = React.useCallback(() => {
    if (gameId) {
      dispatch(Actions.gameEffectsFetch(gameId, point));
    }
  }, [gameId, point]);

  return (
    <div style={style} className={className} onClick={onClick}>
      {children}
    </div>
  );
};
