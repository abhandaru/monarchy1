import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import { useDispatch, useSelector } from 'react-redux';

export default function EffectTile(props) {
  const { children, style, gameId, point, controlled } = props;
  const dispatch = useDispatch();

  console.log('hello')
  const className = classnames(
    styles.tile,
    controlled ? styles.effect : styles.effectNonOwner
  );

  const onClick = React.useCallback(() => {
    if (gameId) {
      // dispatch(Actions.gameSetSelections({ ...selections, effectConfirm: point }));
    }
  }, [gameId, point]);

  return (
    <div style={style} className={className} onClick={onClick}>
      {children}
    </div>
  );
};
