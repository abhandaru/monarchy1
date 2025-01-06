import * as React from 'react';
import DiscordButton from './DiscordButton';
import { host } from '~/util/host';
import Jumbotron from '~/components/Jumbotron';
// @ts-ignore
import styles from './index.css';

function mkAuthorizeUrl(): string {
  const referrer = encodeURIComponent(window.location.href);
  return `${host}/oauth2/discord/authorize?referrer=${referrer}`;
}

const LoginView = (props: {}) => {
  const authorizeUrl = mkAuthorizeUrl();
  return (
    <>
      <Jumbotron>
        <h1>Monarchy</h1>
        <p>This is a low fidelity testing client for the API.</p>
      </Jumbotron>
      <div className={styles.login}>
        <DiscordButton href={authorizeUrl}>
          Log In
        </DiscordButton>
      </div>
    </>
  );
}

export default LoginView;
