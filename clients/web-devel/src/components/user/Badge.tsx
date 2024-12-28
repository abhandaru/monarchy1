import * as React from 'react';
// @ts-ignore
import styles from './index.css';

type Props = {
  color: string;
};

const Badge = (props: Props) => {
  const { color } = props;
  return (
    <div className={styles.badge} style={{ backgroundColor: color }}>
      {color}
    </div>
  );
};

export default Badge;