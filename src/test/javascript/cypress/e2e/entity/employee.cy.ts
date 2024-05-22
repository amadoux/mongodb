import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Employee e2e test', () => {
  const employeePageUrl = '/employee';
  const employeePageUrlPattern = new RegExp('/employee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const employeeSample = { email: 'Jeannel8@yahoo.fr', phoneNumber: 'conseil municipal', identityCard: 'retracer' };

  let employee;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/employees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/employees').as('postEntityRequest');
    cy.intercept('DELETE', '/api/employees/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (employee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/employees/${employee.id}`,
      }).then(() => {
        employee = undefined;
      });
    }
  });

  it('Employees menu should load Employees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('employee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Employee').should('exist');
    cy.url().should('match', employeePageUrlPattern);
  });

  describe('Employee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(employeePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Employee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/employee/new$'));
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/employees',
          body: employeeSample,
        }).then(({ body }) => {
          employee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/employees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/employees?page=0&size=20>; rel="last",<http://localhost/api/employees?page=0&size=20>; rel="first"',
              },
              body: [employee],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(employeePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Employee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('employee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it('edit button click should load edit Employee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it('edit button click should load edit Employee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Employee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);
      });

      it('last delete button click should delete instance of Employee', () => {
        cy.intercept('GET', '/api/employees/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('employee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeePageUrlPattern);

        employee = undefined;
      });
    });
  });

  describe('new Employee page', () => {
    beforeEach(() => {
      cy.visit(`${employeePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Employee');
    });

    it('should create an instance of Employee', () => {
      cy.get(`[data-cy="firstName"]`).type('Alexis');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Alexis');

      cy.get(`[data-cy="lastName"]`).type('Garnier');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Garnier');

      cy.get(`[data-cy="email"]`).type('Rodrigue_Bernard26@hotmail.fr');
      cy.get(`[data-cy="email"]`).should('have.value', 'Rodrigue_Bernard26@hotmail.fr');

      cy.get(`[data-cy="phoneNumber"]`).type('résumer');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'résumer');

      cy.get(`[data-cy="identityCard"]`).type('même si');
      cy.get(`[data-cy="identityCard"]`).should('have.value', 'même si');

      cy.get(`[data-cy="dateInspiration"]`).type('2024-05-22T12:44');
      cy.get(`[data-cy="dateInspiration"]`).blur();
      cy.get(`[data-cy="dateInspiration"]`).should('have.value', '2024-05-22T12:44');

      cy.get(`[data-cy="nationality"]`).select('MALI');

      cy.setFieldImageAsBytesOfEntity('uploadIdentityCard', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="typeEmployed"]`).select('SALARY');

      cy.get(`[data-cy="cityAgency"]`).type('rectorat');
      cy.get(`[data-cy="cityAgency"]`).should('have.value', 'rectorat');

      cy.get(`[data-cy="residenceCity"]`).type('touriste');
      cy.get(`[data-cy="residenceCity"]`).should('have.value', 'touriste');

      cy.get(`[data-cy="address"]`).type('devant');
      cy.get(`[data-cy="address"]`).should('have.value', 'devant');

      cy.get(`[data-cy="socialSecurityNumber"]`).type('miam');
      cy.get(`[data-cy="socialSecurityNumber"]`).should('have.value', 'miam');

      cy.get(`[data-cy="birthDate"]`).type('2024-05-22T06:35');
      cy.get(`[data-cy="birthDate"]`).blur();
      cy.get(`[data-cy="birthDate"]`).should('have.value', '2024-05-22T06:35');

      cy.get(`[data-cy="birthPlace"]`).type('engager juriste');
      cy.get(`[data-cy="birthPlace"]`).should('have.value', 'engager juriste');

      cy.get(`[data-cy="entryDate"]`).type('2024-05-22T11:01');
      cy.get(`[data-cy="entryDate"]`).blur();
      cy.get(`[data-cy="entryDate"]`).should('have.value', '2024-05-22T11:01');

      cy.get(`[data-cy="releaseDate"]`).type('2024-05-22T13:02');
      cy.get(`[data-cy="releaseDate"]`).blur();
      cy.get(`[data-cy="releaseDate"]`).should('have.value', '2024-05-22T13:02');

      cy.get(`[data-cy="workstation"]`).type('bzzz meuh franco');
      cy.get(`[data-cy="workstation"]`).should('have.value', 'bzzz meuh franco');

      cy.get(`[data-cy="descriptionWorkstation"]`).type('psitt');
      cy.get(`[data-cy="descriptionWorkstation"]`).should('have.value', 'psitt');

      cy.get(`[data-cy="department"]`).select('Ressources_Humaines');

      cy.get(`[data-cy="level"]`).select('E');

      cy.get(`[data-cy="coefficient"]`).type('5609');
      cy.get(`[data-cy="coefficient"]`).should('have.value', '5609');

      cy.get(`[data-cy="numberHours"]`).type("d'entre");
      cy.get(`[data-cy="numberHours"]`).should('have.value', "d'entre");

      cy.get(`[data-cy="averageHourlyCost"]`).type('commis');
      cy.get(`[data-cy="averageHourlyCost"]`).should('have.value', 'commis');

      cy.get(`[data-cy="monthlyGrossAmount"]`).type('8135');
      cy.get(`[data-cy="monthlyGrossAmount"]`).should('have.value', '8135');

      cy.get(`[data-cy="commissionAmount"]`).type('14114');
      cy.get(`[data-cy="commissionAmount"]`).should('have.value', '14114');

      cy.get(`[data-cy="contractType"]`).select('COMMERCIAL_AGENT');

      cy.get(`[data-cy="salaryType"]`).select('ASSOCIATE');

      cy.get(`[data-cy="hireDate"]`).type('2024-05-22T16:25');
      cy.get(`[data-cy="hireDate"]`).blur();
      cy.get(`[data-cy="hireDate"]`).should('have.value', '2024-05-22T16:25');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        employee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', employeePageUrlPattern);
    });
  });
});
