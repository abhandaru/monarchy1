import * as Auth from '~/api/auth';
import * as React from 'react';
import Alert from 'react-bootstrap/Alert';
import GamesView from '~/views/GamesView';
import MatchmakingView from '~/views/MatchmakingView';
import NavigationView from '~/components/layout/NavigationView';
// @ts-ignore
import styles from './index.css';
import { State } from '~/state/state';
import { useSelector } from 'react-redux'

type Props = {
  auth: Auth.Auth;
};

const DashboardView = (props: Props) => {
  const { auth } = props;
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
