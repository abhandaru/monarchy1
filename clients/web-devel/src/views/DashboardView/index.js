import * as React from 'react';
import Alert from 'react-bootstrap/Alert';
import GamesView from '~/views/GamesView';
import MatchmakingView from '~/views/MatchmakingView';
import NavigationView from '~/components/layout/NavigationView';
import styles from './index.css';
import { useSelector } from 'react-redux'

const DashboardView = (props) => {
  const auth = useSelector(_ => _.auth);
  return (
    <>
      <NavigationView />
      <div className={styles.root}>
        <Alert variant='primary'>
          Welcome, you are logged in as <b>{auth.user.username}</b>
        </Alert>
        <GamesView />
        <MatchmakingView />
      </div>
    </>
  );
};

export default DashboardView;
