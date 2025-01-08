import * as React from 'react';
// @ts-ignore
import styles from './index.css';
import * as Types from '~/util/types';

type Props = {
  user: Types.User;
  rating?: boolean | number;
};

const Compact = (props: Props) => {
  const { user, rating } = props;
  const color = user.profile?.color ?? '#FFFFFF';
  const ratingOverride = rating && (typeof rating === 'number' ? rating : user.rating);
  return (
    <div className={styles.compact}>
      <div className={styles.compactBadge} style={{ backgroundColor: color }} />
      <div>{user.username}</div>
      {ratingOverride && <div className={styles.compactRating}>@ {ratingOverride}</div>}
    </div>
  );
};

export default Compact;