set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [466.4455294	630.9780512	1047.227228	2263.420394	6142.962699];
y2 = [138.6066673	659.4309571	1889.960021	5528.447065	17398.85076];
y3 = [9.058427296	17.97176025	17.37839507	21.61730666	26.04822192];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the number of keywords in S');
ylabel('time (ms)');

axis([0.5 5.5 0.0 100000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'1','3','5','7','9'});
leg=legend('basic-g','basic-w','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);