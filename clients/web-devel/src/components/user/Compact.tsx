import * as React from 'react';
// @ts-ignore
import styles from './index.css';
import * as Types from '~/util/types';

type Props = {
  user: Types.User;
  rating?: boolean;
};

const Compact = (props: Props) => {
  const { user, rating } = props;
  const color = user.profile?.color ?? '#FFFFFF';
  return (
    <div className={styles.compact}>
      <div className={styles.compactBadge} style={{ backgroundColor: color }} />
      <div>{user.username}</div>
      {rating && <div className={styles.compactRating}>@ {user.rating}</div>}
    </div>
  );
};

export default Compact;