import React, { useState } from 'react';
import { Button, Input, Radio, Spacer, Text } from '@nextui-org/react';
import { Call, Home, Password, User } from 'react-iconly';

import { useForm } from 'react-hook-form';
import { customer } from '@/about/services/customer';

import './About.css';

export const About = () => {
  const { register, handleSubmit } = useForm();
  const [error, setError] = useState<boolean>(false);
  const [success, setSuccess] = useState<any>(false);

  const onSubmit = (loginData: any) => {
    if (loginData.name && loginData.surname && loginData.address && loginData.phoneNumber && loginData.accept) {
      customer({ ...loginData }, setSuccess);
      setError(false);
    } else setError(true);
  };
  return (
    <div className="About">
      <div>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Input
            width="350px"
            clearable
            bordered
            aria-label="modal-login-username"
            data-testid="modal-login-username"
            color="primary"
            size="lg"
            placeholder="Nom"
            contentLeft={<User />}
            {...register('name')}
          />
          {/* dans le ...register t'ajoute le nom de la donnée que tu veux renvoyer au back */}
          <Spacer y={1} />
          <Input
            width="350px"
            clearable
            bordered
            aria-label="modal-login-username"
            data-testid="modal-login-username"
            color="primary"
            size="lg"
            placeholder="Prénom"
            contentLeft={<User />}
            {...register('surname')}
          />
          <Spacer y={1} />
          <Input
            width="350px"
            clearable
            bordered
            aria-label="modal-login-username"
            data-testid="modal-login-username"
            color="primary"
            size="lg"
            placeholder="Numéro de téléphone"
            contentLeft={<Call />}
            {...register('phoneNumber')}
          />
          <Spacer y={1} />
          <Input
            width="350px"
            clearable
            bordered
            aria-label="modal-login-username"
            data-testid="modal-login-username"
            color="primary"
            size="lg"
            placeholder="Adresse"
            contentLeft={<Home />}
            {...register('address')}
          />
          <Spacer y={1} />
          <Radio.Group size="sm" value="homme">
            <label>Sexe</label>
            <Radio size="sm" value="homme">
              Homme
            </Radio>
            <Radio size="sm" value="femme">
              Femme
            </Radio>
            <Radio size="sm" value="autre">
              Autre
            </Radio>
          </Radio.Group>
          <Spacer y={1} />
          <input type="checkbox" {...register('accept')} />
          <label>J'accepte les condtions</label>
          <Spacer y={1} />
          <Button data-testid="submit-button" className="submit-button" type="submit" shadow auto>
            Enregistrer mes infos
          </Button>
          <Spacer y={1} />
          {error && (
            <Text data-testid="error-message" size={13} color="error">
              Veuillez remplir les champs ci dessus
            </Text>
          )}
        </form>
        {success === true && <p>Merci d'avoir renseigné vos informations</p>}
      </div>
    </div>
  );
};
