import * as React from 'react';
// @ts-ignore
import styles from './index.css';
import * as Types from '~/util/types';

const RatingDelta = (props: {delta: number}) => {
  const { delta } = props;
  const formatted = delta > 0 ? `+${delta}` : `${delta}`;
  let color = 'gray';
  if (delta > 0) color = '#28a745';
  else if (delta < 0) color = '#dc3545';
  return <div className={styles.compactRating} style={{ color }}>{formatted}</div>;
};

type Props = {
  user: Types.User;
  rating?: number | boolean;
  ratingDelta?: number | null;
};

const Compact = (props: Props) => {
  const { user, rating, ratingDelta } = props;
  const color = user.profile?.color ?? '#FFFFFF';
  const ratingOverride = rating ? (typeof rating === 'number' ? rating : user.rating) : null;
  return (
    <div className={styles.compact}>
      <div className={styles.compactBadge} style={{ backgroundColor: color }} />
      <div>{user.username}</div>
      {ratingOverride && <div className={styles.compactRating}>@ {ratingOverride}</div>}
      {ratingDelta && <RatingDelta delta={ratingDelta} />}
    </div>
  );
};

export default Compact;