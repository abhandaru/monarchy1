import React from 'react';
import NavigationView from '~/components/layout/NavigationView';
import GamesView from '~/views/GamesView';
// @ts-ignore
import styles from './index.css';

const MatchesView = () => {
  return (
    <>
      <NavigationView />
      <div className={styles.root}>
        <GamesView />
      </div>
    </>
  );
};

export default MatchesView;
