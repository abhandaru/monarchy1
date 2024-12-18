import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import { useDispatch } from 'react-redux';

export default function EffectTile(props) {
  const { children, style, gameId, attack, controlled } = props;
  const dispatch = useDispatch();
  const className = classnames(
    styles.tile,
    controlled ? styles.effect : styles.effectNonOwner
  );

  const onClick = React.useCallback(() => {
    if (gameId && attack) {
      dispatch(Actions.gameAttack(gameId, attack));
    }
  }, [gameId, attack]);

  return (
    <div style={style} className={className} onClick={onClick}>
      {children}
    </div>
  );
};
