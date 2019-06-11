export class User {
  id: number;
  givenName: string;
  familyName: string;
  mail: string;
  roles: string[];

  static hasRole(user: User, role: string) {
    return user.roles.some(r=>r==role);
  }

}
