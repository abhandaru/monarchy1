import * as Auth from '~/api/auth';
import * as React from 'react';
import NavigationView from '~/components/layout/NavigationView';
import { useSelector } from 'react-redux';
// @ts-ignore
import styles from './index.css';
import { State } from '~/state/state';

const ProfileView = () => {
  const auth = useSelector<State, State['auth']>(_ => _.auth);
  const { user } = auth;
  return (
    <>
      <NavigationView />
      <pre>{JSON.stringify(user, null, 2)}</pre>
    </>
  );
};

export default ProfileView;