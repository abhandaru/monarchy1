import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import { useDispatch, useSelector } from 'react-redux';

export default function AttackTile(props) {
  const { children, style, gameId, point, controlled, attack } = props;
  const dispatch = useDispatch();
  const className = classnames(
    styles.tile,
    controlled ? styles.attack : styles.attackNonOwner
  );

  const onClick = React.useCallback(() => {
    if (gameId && attack) {
      dispatch(Actions.gameEffectsFetch(gameId, attack));
    }
  }, [gameId, attack]);

  return (
    <div style={style} className={className} onClick={onClick}>
      {children}
    </div>
  );
};
