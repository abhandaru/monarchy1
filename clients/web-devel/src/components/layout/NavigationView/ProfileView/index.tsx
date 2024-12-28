import * as React from 'react';
import Compact from '~/components/user/Compact';
// @ts-ignore
import styles from './index.css';
import { State } from '~/state/state';
import { useSelector } from 'react-redux'

const ProfileView = (props) => {
  const auth = useSelector<State, State['auth']>(_ => _.auth);
  return (
    <div className={styles.root}>
      <Compact user={auth.user} />
    </div>
  );
};

export default ProfileView;
