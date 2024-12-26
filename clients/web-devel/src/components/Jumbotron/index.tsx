import * as React from 'react';
// @ts-ignore
import styles from './index.css';

type Props = {
  children: React.ReactNode;
};

const Jumbotron = (props: Props) => {
  return (
    <div className={styles.root}>
      {props.children}
    </div>
  );
};

export default Jumbotron;
