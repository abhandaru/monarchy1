import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import writeMove from '~/api/writeMove';
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
      writeMove({ gameId, point }).then(r => {
        dispatch(Actions.gameSetSelections(r.data.select));
      });
    }
  }, [gameId, point]);

  return (
    <div style={style} className={className} onClick={onClick}>
      {children}
    </div>
  );
};
