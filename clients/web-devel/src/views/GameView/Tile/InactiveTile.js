import * as Actions from '~/state/actions';
import * as React from 'react';
import classnames from 'classnames';
import styles from './index.css';
import writeSelect from '~/api/writeSelect';
import { useDispatch } from 'react-redux';

export default function InactiveTile(props) {
  const { children, style, gameId, point } = props;
  const className = classnames(styles.tile, styles.inactive);
  const dispatch = useDispatch();
  const onClick = React.useCallback(() => {
    if (gameId) {
      writeSelect({ gameId, point }).then(r => {
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
