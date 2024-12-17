import * as React from 'react';
import Badge from 'react-bootstrap/Badge';
import styles from './index.css';
import { useSelector } from 'react-redux'

const ProfileView = (props) => {
  const auth = useSelector(_ => _.auth);
  const username = auth.user?.username;

  return (
    <div className={styles.root}>
      <div>{username}</div>
    </div>
  );
};

export default ProfileView;
