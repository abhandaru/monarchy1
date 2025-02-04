import * as React from 'react';
import ConnectionView from './ConnectionView';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import ProfileView from './ProfileView';
import { Link } from 'react-router-dom';

const NavigationView = (props) => {
  return (
    <Navbar bg='light' variant='light'>
      <Navbar.Brand href='/'>Monarchy</Navbar.Brand>
      <Navbar.Collapse>
        <Nav className='mr-auto'>
          <Link to='/matches'>Matches</Link>
        </Nav>
        <ConnectionView />
        <ProfileView />
      </Navbar.Collapse>
    </Navbar>
  );
};

export default NavigationView;
