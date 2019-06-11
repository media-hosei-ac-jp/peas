import { User } from './user';
import { Quiz } from './quiz';
export class Target {
  id: number;
  displayName: string;
  resultPublished: boolean;
  checked: Date;
  active: boolean;
  targeted: boolean;
  users: User[] = [];
  quiz: Quiz;
}
